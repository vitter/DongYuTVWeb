(function() {
    let count = 1

    if (!window.playListener) {
     window.playListener = function(e) {
             console.log('video: onPlay')
             window.isPlaying = true
              window.AndroidVideo.onPlay()
        }
    }

    if (!window.pauseListener) {
        window.pauseListener = function(e) {
            window.isPlaying = false
            console.log('video: onPause')
            window.AndroidVideo.onPause()
        }
    }

    /*if (!window.keyListener) {
        window.keyListener = function(e) {
            e.preventDefault();
            console.log('onKeyDown' + e);
            window.AndroidVideo.onKeyDown(e.key, e.keyCode)
        }
    }*/

    function load() {
        const video = document.querySelector('video')

         if (video) {
            video.volume = 1

             video.addEventListener('play', window.playListener)

             video.addEventListener('pause', window.pauseListener)

             // document.addEventListener('keydown', window.keyListener)

             console.log('setup complete')

             return
         }

         if (count++ > 10) {
            return
         }

         setTimeout(function() {
            load()
         }, 200)
    }

    load()

})();