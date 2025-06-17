export default class Volume {
    constructor(video, volumeBtn, volumeSlider, volumeProgress) {
        this.video = video;
        this.volumeBtn = volumeBtn;
        this.volumeSlider = volumeSlider;
        this.volumeProgress = volumeProgress;

        // 드래그를 통한 볼륨 조절 기능
        this.isDraggingVolume = false;

        this.addEvent();
    }

    addEvent() {
        this.video.addEventListener('loadedmetadata', () => {
           this.#loadLocalVolumeData();
        });

        // 볼륨 음소거 토글
        this.volumeBtn.addEventListener('click', () => {
           this.video.muted = !this.video.muted;
           this.updateVolumeIcon(this.video.muted? 0 : this.video.volume);
           this.#drawVolumeProgress(this.video.muted? 0: this.video.volume);
        });

        // 볼륨 조절
        this.volumeSlider.addEventListener('click', (e) => {
            const position = this.#getPosition(e);
            this.video.volume = position;
            this.#drawVolumeProgress(position);
            this.updateVolumeIcon(position);
        });

        // 볼륨 슬라이더 드래그 이벤트
        this.volumeSlider.addEventListener('mousedown', (e) => {
            this.isDraggingVolume = true;
            this.handleVolumeChange(e);
        });

        document.addEventListener('mousemove', (e) => {
           if (this.isDraggingVolume) this.handleVolumeChange(e);
        });

        document.addEventListener('mouseup', () => {
            this.isDraggingVolume = false;
        });
    }

    #loadLocalVolumeData() {
        const savedVolume = localStorage.getItem('videoVolume');
        let volume = parseFloat(savedVolume);
        this.video.volume = volume;

        if (this.video.muted) {
            volume = 0;
        }

        this.#drawVolumeProgress(volume);
        this.updateVolumeIcon(volume);
    }

    #drawVolumeProgress(volume) {
        this.volumeProgress.style.width = (volume * 100) + '%';
    }

    #getPosition(e) {
        const rect = this.volumeSlider.getBoundingClientRect();
        let position = (e.clientX - rect.left) / rect.width;
        return Math.max(0, Math.min(1, position));
    }

    updateVolumeIcon(volume) {
        const volumeLevel = Math.floor(volume * 100);
        if (volumeLevel === 0) {
            this.volumeBtn.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M301.1 34.8C312.6 40 320 51.4 320 64l0 384c0 12.6-7.4 24-18.9 29.2s-25 3.1-34.4-5.3L131.8 352 64 352c-35.3 0-64-28.7-64-64l0-64c0-35.3 28.7-64 64-64l67.8 0L266.7 40.1c9.4-8.4 22.9-10.4 34.4-5.3zM425 167l55 55 55-55c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9l-55 55 55 55c9.4 9.4 9.4 24.6 0 33.9s-24.6 9.4-33.9 0l-55-55-55 55c-9.4 9.4-24.6 9.4-33.9 0s-9.4-24.6 0-33.9l55-55-55-55c-9.4-9.4-9.4-24.6 0-33.9s24.6-9.4 33.9 0z"/></svg>';
        } else if (volumeLevel <= 50) {
            this.volumeBtn.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M301.1 34.8C312.6 40 320 51.4 320 64l0 384c0 12.6-7.4 24-18.9 29.2s-25 3.1-34.4-5.3L131.8 352 64 352c-35.3 0-64-28.7-64-64l0-64c0-35.3 28.7-64 64-64l67.8 0L266.7 40.1c9.4-8.4 22.9-10.4 34.4-5.3zM412.6 181.5C434.1 199.1 448 225.9 448 256s-13.9 56.9-35.4 74.5c-10.3 8.4-25.4 6.8-33.8-3.5s-6.8-25.4 3.5-33.8C393.1 284.4 400 271 400 256s-6.9-28.4-17.7-37.3c-10.3-8.4-11.8-23.5-3.5-33.8s23.5-11.8 33.8-3.5z"/></svg>';
        } else {
            this.volumeBtn.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M333.1 34.8C312.6 40 320 51.4 320 64l0 384c0 12.6-7.4 24-18.9 29.2s-25 3.1-34.4-5.3L131.8 352 64 352c-35.3 0-64-28.7-64-64l0-64c0-35.3 28.7-64 64-64l67.8 0L266.7 40.1c9.4-8.4 22.9-10.4 34.4-5.3zm172 72.2c43.2 35.2 70.9 88.9 70.9 149s-27.7 113.8-70.9 149c-10.3 8.4-25.4 6.8-33.8-3.5s-6.8-25.4 3.5-33.8C507.3 341.3 528 301.1 528 256s-20.7-85.3-53.2-111.8c-10.3-8.4-11.8-23.5-3.5-33.8s23.5-11.8 33.8-3.5zm-60.5 74.5C466.1 199.1 480 225.9 480 256s-13.9 56.9-35.4 74.5c-10.3 8.4-25.4 6.8-33.8-3.5s-6.8-25.4 3.5-33.8C425.1 284.4 432 271 432 256s-6.9-28.4-17.7-37.3c-10.3-8.4-11.8-23.5-3.5-33.8s23.5-11.8 33.8-3.5z"/></svg>';
        }
    }

    handleVolumeChange(e) {
        let position = this.#getPosition(e);
        this.video.volume = position;
        this.video.muted = position === 0;
        this.#drawVolumeProgress(position);
        this.updateVolumeIcon(position);

        // 볼륨 설정 저장
        localStorage.setItem('videoVolume', this.video.volume);
    }
}