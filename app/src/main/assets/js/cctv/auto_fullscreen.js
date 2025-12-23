(function AutoFullscreen() {
    var fullscreenBtn = document.querySelector('#player_pagefullscreen_yes_player');
    if (fullscreenBtn != null) {
        fullscreenBtn.click();
    } else {
        setTimeout(function() {
            AutoFullscreen();
        }, 16)
    }
})();