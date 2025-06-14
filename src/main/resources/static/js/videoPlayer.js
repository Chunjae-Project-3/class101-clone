import Video from "./Video.js";

window.addEventListener('load', () => {
    const videoEl = document.querySelector('#videoPlayer');
    const videoId = videoEl.getAttribute('aria-id');
    const playlistUrl = `/api/video/${videoId}/master.m3u8`;

    const video = new Video(
        videoEl,
        document.querySelector('.panel'),
        playlistUrl
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
    video.autoPlay();
});