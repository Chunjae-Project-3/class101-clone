import Video from "./Video";

window.addEventListener('load', () => {
    const videoEl = document.querySelector('#videoPlayer');
    const videoId = videoEl.getAttribute('aria-id');
    const playlistUrl = '';

    const video = new Video(
        videoEl,
        playlistUrl,
        document.querySelector('.panel')
    );

    video.setPlayButton(
        document.querySelector('.thumbnail-box'),
        document.querySelector('#playButton')
    );
    video.controller.setTimeline(
        document.querySelector('.timeline'),
        document.querySelector('.progress'),
        document.querySelector('.time-display')
    );
    video.controller.setPlayPauseBtn(
        document.querySelector('.play-pause'),
        document.querySelector('.play-pause-event')
    );
    video.controller.setKeyShortCuts();
    video.initialEventListener();
    // video.autoPlay();
});