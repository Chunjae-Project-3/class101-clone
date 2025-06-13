import Timeline from "./Timeline";
import PlayPause from "./PlayPause";
import Volume from "./Volume";
import Quality from "./Quality";
import FullScreen from "./FullScreen";

export default class Controller {
    constructor(hls, videoEl, loadingSpinnerEl, panelEl) {
        this.hls = hls;
        this.video = videoEl;
        this.loadingSpinner = loadingSpinnerEl;
        this.panel = panelEl;
    }

    // 초기 이벤트 바인딩
    initialDependenciesEventListener() {
        this.timeline.setDraggingEvent(this.playPause);
    }

    // 타임라인
    setTimeline(timelineEl, progressEl, timeDisplayEl) {
        this.timeline = new Timeline(this.video, timelineEl, progressEl, timeDisplayEl)
    }

    // 재생, 일시정지 버튼
    setPlayPauseBtn(playPauseBtnEl, playPauseEventEl) {
        this.playPause = new PlayPause(this.video, this.panel, playPauseBtnEl, playPauseEventEl);
    }

    // 볼륨 조절 버튼
    setVolume(volumeBtnEl, volumeSliderEl, volumeProgressEl) {
        this.volume = new Volume(this.video, this.panel, volumeBtnEl, volumeSliderEl, volumeProgressEl);
    }

    // 화질 설정
    setQuality(qualityBtnEl, qualityOptionBoxEl) {
        this.quality = new Quality(qualityBtnEl, qualityOptionBoxEl);

        // 화질 초기화
        this.hls.on(Hls.Events.MANIFEST_PARSED, (event, data) => {
            console.log('Available qualities:', data.levels);

            // 사용 가능한 화질 옵션 생성
            this.quality.createQualityOptions(data.levels);
            this.quality.addEventChangeQuality(this.hls, this.playPause, this.loadingSpinner);

            // 현재 화질 설정
            let currentLevel = this.hls.currentLevel;
            this.quality.updateQualityButton(currentLevel, data.levels);

            // 자동 화질 선택 모드 설정
            this.hls.currentLevel = -1;
        });

        // 화질 변경 이벤트
        this.hls.on(Hls.Events.LEVEL_SWITCHED, (event, data) => {
            this.quality.updateQualityButton(data.level, data.levels);
        });
    }

    // 전체 화면 설정
    setFullScreen(fullscreenBtnEl) {
        this.fullScreen = new FullScreen(this.video, fullscreenBtnEl);
    }

    // 단축키 설정
    setKeyShortCuts() {
        document.addEventListener('keydown', (e) => {
            // 입력 필드에 포커스가 있을 때는 단축키 비활성화
            if (document.activeElement.tagName === 'INPUT' ||
                document.activeElement.tagName === 'TEXTAREA') {
                return;
            }
            this.playPause.keyShortcuts(e, 10, 10);
            this.fullScreen.keyShortcuts(e);
        });
    }
}