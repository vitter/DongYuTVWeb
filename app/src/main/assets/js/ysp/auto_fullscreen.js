(function AutoFullscreen() {
    var fullscreenBtn = document.querySelector('.videoFull');
    if (fullscreenBtn != null) {
        fullscreenBtn.click();
    } else {
        setTimeout(function() {
            AutoFullscreen();
        }, 16)
    }
})();