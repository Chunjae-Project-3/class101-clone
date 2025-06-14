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

    initialEventListener() {
        this.controller.initialDependenciesEventListener();
    }

    #initPlayButtonEvent() {
        this.thumbnail.remove();
        this.controller.panel.classList.remove('disabled');
        this.play();
    }

    async autoPlay() {
        this.hls.loadSource(this.playlistUrl);
        this.hls.attachMedia(this.video);

        if (await this.play()) {
            this.thumbnail.remove();
            this.controller.panel.classList.remove('disabled');
        } else {
            this.playButton.addEventListener('click', () => {
                this.#initPlayButtonEvent();
            }, {once : true})
        }
    }

    async play() {
        return await this.controller.playPause.play();
    }

    pause() {
        this.controller.playPause.pause();
    }
}