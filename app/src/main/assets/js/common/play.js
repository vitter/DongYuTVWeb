(function() {
function play() {
   const video = document.querySelector('video')

    if (video) {
        console.log('play')
       video.play()
    }
}
play()

})();