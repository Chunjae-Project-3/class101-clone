export default class Timeline {
    constructor(videoEl, timelineEl, progressEl, timeDisplayEl) {
        this.video = videoEl;
        this.timeline = timelineEl;
        this.progress = progressEl;
        this.bufferProgress = this.timeline.querySelector('.buffer-progress');
        this.timeDisplay = timeDisplayEl;
        this.isDraggingProgress = false;
        this.currentTimelineLocation = 0;
        this.#addEvent();
    }

    #addEvent() {
        // 타임라인 클릭
        this.timeline.addEventListener('click', (e) => {
            this.video.currentTime = this.#getProgressOnClick(e);
        });

        // 타임라인 업데이트
        this.video.addEventListener('timeupdate', () => {
            this.updateProgress(this.video.currentTime, this.video.duration);
        });
    }

    setDraggingEvent(playPause) {
        // 버퍼 상태 업데이트
        this.video.addEventListener('progress', () => {
            this.#updateBufferProgress();
        });

        // 타임라인 드래그 이벤트
        this.timeline.addEventListener('mousedown', (e) => {
            this.isDraggingProgress = true;
            playPause.pause();
            this.currentTimelineLocation = this.#getProgressOnClick(e);
        });

        document.addEventListener('mousemove', (e) => {
            if (this.isDraggingProgress) {
                this.currentTimelineLocation = this.#getProgressOnClick(e);
                this.updateProgress(this.currentTimelineLocation, this.video.duration);
            }
        });

        document.addEventListener('mouseup', () => {
            if (this.isDraggingProgress) {
                this.isDraggingProgress = false;
                this.video.currentTime = this.currentTimelineLocation;
                playPause.play();
            }
        });
    }

    #getProgressOnClick(e) {
        const rect = this.timeline.getBoundingClientRect();
        const pos = (e.clientX - rect.left) / rect.width;
        return pos * this.video.duration;
    }

    updateProgress(current, total) {
        this.timeDisplay.textContent = `${this.#formatTime(current)} / ${this.#formatTime(total)}`;
        this.progress.style.width = (current / total) * 100 + '%';
    }

    #updateBufferProgress() {
        if (this.video.buffered.length > 0) {
            const bufferedEnd = this.video.buffered.end(this.video.buffered.length - 1);
            const duration = this.video.duration;
            this.bufferProgress.style.width = ((bufferedEnd / duration) * 100) + '%';
        }
    }

    #formatTime(seconds) {
        if (isNaN(seconds)) return '--:--';
        const minutes = Math.floor(seconds / 60);
        seconds = Math.floor(seconds % 60);
        return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    }
}