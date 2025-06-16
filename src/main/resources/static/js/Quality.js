export default class Quality {
    constructor(qualityBtnElement, qualityOptionsBox) {
        this.qualityBtn = qualityBtnElement;
        this.qualityOptionsBox = qualityOptionsBox;

        this.#addEvent();
    }

    #addEvent() {
        // 선택 후 박스 감춤 이벤트
        this.qualityBtn.addEventListener('click', () => {
            this.qualityOptionsBox.classList.toggle('disabled');
        });
        document.addEventListener('click', (e) => {
            if (e.target !== this.qualityOptionsBox && e.target !== this.qualityBtn) {
                this.qualityOptionsBox.classList.add('disabled');
            }
        });
    }

    createQualityOptions(levels) {
        this.qualityOptionsBox.innerHTML = '';

        // AUTO 옵션
        const autoButton = document.createElement('button');
        autoButton.setAttribute('data-quality', '-1');
        autoButton.textContent = 'AUTO';
        this.qualityOptionsBox.appendChild(autoButton);

        // 높은 화질부터 추가
        for (let i = levels.length - 1; i >= 0; i--) {
            const level = levels[i];
            const resolution = level.height + 'p';
            const button = document.createElement('button');
            button.setAttribute('data-quality', i.toString());
            button.textContent = resolution;
            this.qualityOptionsBox.appendChild(button);
        }
    }

    updateQualityButton(level, levels) {
        if (level === -1) {
            this.qualityBtn.textContent = 'AUTO';
        } else if (levels && level < levels.length) {
            this.qualityBtn.textContent = levels[level].height + 'p';
        }
        this.qualityOptionsBox.classList.add('disabled');
    }

    addEventChangeQuality(hls, playPause, loadingSpinner) {
        const qualityOptions = this.qualityOptionsBox.querySelectorAll('button');
        qualityOptions.forEach(button => {
            button.addEventListener('click', () => {
                const quality = parseInt(button.getAttribute('data-quality'));

                if (quality === hls.currentLevel) {
                    this.qualityOptionsBox.classList.add('disabled');
                    return;
                }

                loadingSpinner.classList.remove('disabled');

                if (quality === -1) {
                    // AUTO 모드 활성화
                    hls.currentLevel = -1;
                    hls.loadLevel = -1;
                    hls.config.startLevel = -1;  // 자동 품질 선택
                } else {
                    // 수동으로 특정 품질 선택
                    hls.currentLevel = quality;
                    hls.loadLevel = quality;
                }

                this.updateQualityButton(quality, hls.levels);

                const onFragLoaded = (event, data) => {
                    loadingSpinner.classList.add('disabled');
                    hls.off(Hls.Events.FRAG_LOADED, onFragLoaded);
                };

                hls.on(Hls.Events.FRAG_LOADED, onFragLoaded);
            });
        });
    }
}