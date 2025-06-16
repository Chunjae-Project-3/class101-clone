export default class FullScreen {
    constructor(video, fullscreenBtn) {
        this.video = video;
        this.fullScreen = fullscreenBtn;
        this.#addEvent();
    }

    #addEvent() {
        this.fullScreen.addEventListener('click', () => this.#toggleFullScreen());
    }

    #toggleFullScreen() {
        document.fullscreenElement ? this.#fullScreenExit() : this.#fullScreen();
    }

    #fullScreen() {
        this.video.parentElement.requestFullscreen();
    }

    #fullScreenExit() {
        document.exitFullscreen();
    }

    keyShortcuts(e) {
        if (e.code === 'KeyF') {
            this.#toggleFullScreen();
        }
    }
}