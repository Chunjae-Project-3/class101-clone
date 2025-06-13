export default class PlayPause {
    constructor(videoEl, panelEl, playPauseBtnEl, playPauseEventEl) {
        this.video = videoEl;
        this.panel = panelEl;
        this.playPauseBtn = playPauseBtnEl;
        this.playPauseEvent = playPauseEventEl;
        this.#addEvent();
    }

    #addEvent() {
        this.playPauseBtn.addEventListener('click', () => {
           this.#playPauseToggle();
        });

        this.panel.addEventListener('click', (e) => {
            if (e.target !== this.panel) return;
            this.#playPauseToggle();
            this.#showEventAnimation();
        });

        // 비디오 종료 시 play -> reload 로 버튼 변경
        this.video.addEventListener('ended', () => {
            this.playPauseBtn.innerHTML = this.getReloadSvg();
        });
    }

    #playPauseToggle() {
        this.isPaused() ? this.play() : this.pause();
    }

    #showEventAnimation() {
        // 이미 실행 중인 애니메이션이 있을 경우 강제중단
        if (this.activeTimer) {
            clearTimeout(this.activeTimer);
            this.playPauseEvent.classList.remove('active');
            void this.playPauseEvent.offsetWidth;
        }

        this.playPauseEvent.classList.add('active');

        this.activeTimer = setTimeout(() => {
            this.playPauseEvent.classList.remove('active');
            this.activeTimer = null;
        }, 700);
    }

    // 단축키 설정
    // increaseAmount : number n초 후로 비디오 타임라인 이동
    // decreaseAmount : number n초 전으로 비디오 타임라인 이동
    keyShortcuts(e, increaseAmount, decreaseAmount) {
        if (e.code === 'ArrowRight') {
            // 오른쪽 화살표 (n초 앞으로)
            this.video.currentTime = Math.min(this.video.duration, this.video.currentTime + increaseAmount);
        } else if (e.code === 'ArrowLeft') {
            // 왼쪽 화살표 (n초 뒤로)
            this.video.currentTime = Math.max(0, this.video.currentTime - decreaseAmount);
        } else if (e.code === 'Space') {
            // 시작, 일시정지
            this.#playPauseToggle();
        }
    }

    async play() {
        try {
            let isPlay = await this.video.play();
            this.playPauseBtn.innerHTML = this.getPauseSvg();
            this.playPauseEvent.innerHTML = this.getPauseSvg();
            return isPlay;
        } catch (e) {
            return false;
        }
    }

    pause() {
        this.video.pause();
        this.playPauseBtn.innerHTML = this.getPlaySvg();
        this.playPauseEvent.innerHTML = this.getPlaySvg();
    }

    isPaused() {
        return this.video.paused;
    }

    getPauseSvg() {
        return '<svg class="animate-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><path d="M48 64C21.5 64 0 85.5 0 112L0 400c0 26.5 21.5 48 48 48l32 0c26.5 0 48-21.5 48-48l0-288c0-26.5-21.5-48-48-48L48 64zm192 0c-26.5 0-48 21.5-48 48l0 288c0 26.5 21.5 48 48 48l32 0c26.5 0 48-21.5 48-48l0-288c0-26.5-21.5-48-48-48l-32 0z"/></svg>';
    }
    getPlaySvg() {
        return '<svg class="animate-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512"><path d="M73 39c-14.8-9.1-33.4-9.4-48.5-.9S0 62.6 0 80L0 432c0 17.4 9.4 33.4 24.5 41.9s33.7 8.1 48.5-.9L361 297c14.3-8.7 23-24.2 23-41s-8.7-32.2-23-41L73 39z"/></svg>';
    }
    getReloadSvg() {
        return '<svg class="animate-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M463.5 224H472c13.3 0 24-10.7 24-24V72c0-9.7-5.8-18.5-14.8-22.2s-19.3-1.7-26.2 5.2L413.4 96.6c-87.6-86.5-228.7-86.2-315.8 1c-87.5 87.5-87.5 229.3 0 316.8s229.3 87.5 316.8 0c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0c-62.5 62.5-163.8 62.5-226.3 0s-62.5-163.8 0-226.3c62.2-62.2 162.7-62.5 225.3-1L327 183c-6.9 6.9-8.9 17.2-5.2 26.2s12.5 14.8 22.2 14.8H463.5z"/></svg>';
    }
}