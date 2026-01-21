/**
* 广西卫视
*/
(function() {
    const videoDiv = document.querySelector('#DivVideo')
    const scaleX = window.innerWidth / 940
    const scaleY = window.innerHeight / 570
    // videoDiv.style.transform = `scale(${scaleX}, ${scaleY})`
    console.log('scaleX:' + scaleX + 'scaleY:' + scaleY);

    videoDiv.style.position = "fixed"
    videoDiv.style.top = "0"
    videoDiv.style.left = "0"
    videoDiv.style.width = `${940 * scaleX}px`
    videoDiv.style.height = `${570 * scaleY}px`
    document.querySelector('.Gxntv_nav').style.display = 'none'
})();