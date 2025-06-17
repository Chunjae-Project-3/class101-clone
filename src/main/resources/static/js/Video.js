import Controller from "./Controller.js";

export default class Video {
    constructor(videoEl, panelEl, playlistUrl) {
        if (!this.#isSupported()) return;
        this.video = videoEl;
        this.playlistUrl = playlistUrl;
        this.loadingSpinner = document.querySelector('.loading-spinner');

        this.#setHls();
        this.controller = new Controller(this.hls, this.video, this.loadingSpinner, panelEl);
        this.#addLoadingEvent();
    }

    #isSupported() {
        return Hls.isSupported();
    }

    #setHls() {
        this.hls = new Hls();
    }

    #addLoadingEvent() {
        // 로딩 시작
        this.video.addEventListener('waiting', () => {
            this.loadingSpinner.classList.remove('disabled');
            this.controller.panel.classList.add('active');
        });

        // 로딩 완료
        this.video.addEventListener('canplay', () => {
            this.loadingSpinner.classList.add('disabled');
            this.controller.panel.classList.remove('active');
        });
    }

    setPlayButton(thumbnailBoxEl, playButtonEl) {
        this.thumbnail = thumbnailBoxEl;
        this.playButton = playButtonEl;
    }

    setCurrentTime(seconds) {
        if (!this.video || isNaN(seconds)) return;

        this.video.addEventListener('loadedmetadata', () => {
            this.video.currentTime = seconds;
        }, { once: true });

        if (this.video.readyState >= 1) {
            this.video.currentTime = seconds;
        }
    }

    initialEventListener() {
        this.controller.initialDependenciesEventListener();
    }

    initialPlayButton() {
        if (!this.playButton || !this.thumbnail) return;

        this.playButton.addEventListener('click', () => {
            this.#initPlayButtonEvent();
        }, { once: true });
    }

    #initPlayButtonEvent() {
        this.thumbnail.remove();
        this.controller.panel.classList.remove('disabled');

        this.hls.loadSource(this.playlistUrl);
        this.hls.attachMedia(this.video);

        this.play();
    }

    async play() {
        return await this.controller.playPause.play();
    }

    pause() {
        this.controller.playPause.pause();
    }

    autoPlay() {
        // this.video.muted = true; // 자동 재생 정책 우회 (음소거)
        this.#initPlayButtonEvent();
    }
}