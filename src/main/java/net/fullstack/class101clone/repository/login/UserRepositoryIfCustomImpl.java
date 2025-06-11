package net.fullstack.class101clone.repository.login;

import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.QUserEntity;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log4j2
@Repository
public class UserRepositoryIfCustomImpl extends QuerydslRepositorySupport implements UserRepositoryIfCustom {
	@Autowired
	private EntityManager em;

	public UserRepositoryIfCustomImpl() {
		super(UserEntity.class);
	}

	/**
	 * 회원가입 기능
	 *
	 * @param userDTO
	 * @return true if signup is successful, false otherwise
	 */
	@Override
	@Transactional
	public boolean signup(UserDTO userDTO) {
		if (existsByUserId(userDTO.getUserId())) {
			return false; // 아이디가 이미 존재하는 경우
		}
		// 이름을 입력하지 않은 경우 임의로 생성
		if (userDTO.getUserName() == null || userDTO.getUserName().isEmpty()) {
			userDTO.setUserName(generateDefaultName());
		}

		UserEntity user = UserEntity.builder()
				.userId(userDTO.getUserId())
				.userPwd(hashPassword(userDTO.getUserPwd())) // 비밀번호 암호화
				.userName(userDTO.getUserName())
				.build();
		try {
			em.persist(user); // JPA를 사용하여 회원 정보 저장
			return true; // 회원가입 성공
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return false;
	}

	/**
	 * 회원가입 시 아이디 중복 체크
	 *
	 * @param userId
	 * @return true if exists, false otherwise
	 */
	@Override
	public boolean existsByUserId(String userId) {
		QUserEntity user = QUserEntity.userEntity;

		return from(user)
				.where(user.userId.eq(userId))
				.fetchCount() > 0;
	}

	/**
	 * 로그인 기능
	 *
	 * @param userDTO
	 * @return UserEntity if login is successful, null otherwise
	 */
	@Override
	public UserEntity login(UserDTO userDTO) {
		QUserEntity user = QUserEntity.userEntity;
		JPQLQuery<UserEntity> query = from(user);
		query.where(user.userId.eq(userDTO.getUserId())
				.and(user.userPwd.eq(hashPassword(userDTO.getUserPwd())))); // 비밀번호 비교 필요

		UserEntity result = query.fetchOne();
		return result; // 로그인 성공
	}

	@Override
	@Transactional
	public boolean quit(String userId) {
		QUserEntity user = QUserEntity.userEntity;
		JPQLQuery<UserEntity> query = from(QUserEntity.userEntity)
				.where(user.userId.eq(userId));
		if (query.fetchCount() == 0) {
			return false; // 해당 아이디가 존재하지 않는 경우
		} else {
			em.remove(em.find(UserEntity.class, userId));
			return true;
		}
	}

	@Override
	@Transactional
	public UserEntity updateUserInfo(UserDTO userDTO) {
		log.info("NEW user info {}", userDTO);

		QUserEntity user = QUserEntity.userEntity;
		// userId로 기존 사용자 조회
		JPQLQuery<UserEntity> query = from(user)
				.where(user.userId.eq(userDTO.getUserId()));
		UserEntity originalUser = query.fetchOne();
		log.info("Original user info {}", originalUser);

		if (originalUser != null) {
			// 비밀번호가 변경된 경우
			if (userDTO.getUserPwd() != null && !userDTO.getUserPwd().isEmpty()) {
				originalUser.setUserPwd(hashPassword(userDTO.getUserPwd()));
			}
			// 이름이 변경된 경우
			if (userDTO.getUserName() != null && !userDTO.getUserName().isEmpty()) {
				originalUser.setUserName(userDTO.getUserName());
			}
			em.merge(originalUser); // JPA를 사용하여 회원 정보 수정
			return originalUser; // 수정된 회원 정보 반환
		}

		return null;
	}

	/**
	 * 비밀번호 암호화 메소드
	 *
	 * @param password
	 * @return hashed password as a String
	 */
	public String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	// 이름 랜덤 생성
	public String generateDefaultName() {
		String[] animals = {
				"사자", "호랑이", "코끼리", "기린", "하마", "코뿔소", "얼룩말", "치타", "표범", "늑대",
				"여우", "곰", "토끼", "다람쥐", "고슴도치", "두더지", "너구리", "스컹크", "오소리", "수달",
				"비버", "사슴", "노루", "염소", "양", "소", "말", "돼지", "닭", "오리",
				"거위", "칠면조", "공작", "비둘기", "참새", "까치", "까마귀", "부엉이", "올빼미", "독수리",
				"매", "황새", "두루미", "펭귄", "갈매기", "백조", "앵무새", "타조", "플라밍고", "딱따구리",
				"앵무새", "참새", "직박구리", "벌새", "까치", "까마귀", "부엉이", "올빼미", "독수리", "매",
				"개", "고양이", "햄스터", "기니피그", "친칠라", "페럿", "토끼", "거북이", "이구아나", "카멜레온",
				"뱀", "도마뱀", "악어", "개구리", "두꺼비", "도롱뇽", "물고기", "금붕어", "잉어", "피라냐",
				"상어", "고래", "돌고래", "바다사자", "물개", "해마", "문어", "오징어", "게", "가재",
				"새우", "조개", "소라", "불가사리", "해파리", "산호", "말미잘", "고슴도치", "복어", "가오리"
		};
		int randomIndex = (int) (Math.random() * 100);
		int randomNumber = (int) (Math.random() * 10000) + 1;
		StringBuilder sb = new StringBuilder();
		sb.append("익명의 ").append(animals[randomIndex]).append(randomNumber);
		return sb.toString();
	}
}
