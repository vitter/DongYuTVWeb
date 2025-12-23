// 清空所有图片的 src 属性，阻止图片加载
//document.querySelectorAll(".column_wrapper img").forEach(img => {
//    img.src = ''
//})

 // 清空特定的脚本 src 属性
// const scriptKeywords = ['login', 'index', 'daohang', 'grey', 'jquery'];
// Array.from(document.getElementsByTagName('script')).forEach(script => {
//     if (scriptKeywords.some(keyword => script.src.includes(keyword))) {
//         script.src = '';
//     }
// });

 // 清空具有特定 class 的 div 内容
 const classNames = ['newmap', 'newtopbz', 'newtopbzTV', 'column_wrapper', 'vspace'];
 classNames.forEach(className => {
     Array.from(document.getElementsByClassName(className)).forEach(div => {
         div.style.display = 'none'
     });
 });

 const pageBottom = document.getElementById("page_bottom")

 if (pageBottom) {
    pageBottom.style.display = 'none'
 }

 const playVideo = document.querySelector(".playingVideo")

if (playVideo) {
  playVideo.style.margin = ''
  playVideo.style.height = '100vh'
  playVideo.style.width = '100vw'
}

 const player = document.getElementById("player")

if (player) {
 player.style.width = '100vw'
 player.style.height = '100vh'
}

const videoBtnBar = document.querySelector('.video_btnBar')
if (videoBtnBar) {
 videoBtnBar.style.display = 'none'
}

const fullscreenBtn = document.querySelector("#player_fullscreen_player")

if (fullscreenBtn) {
    fullscreenBtn.onclick = null
}

const floatNav = document.querySelector(".floatNav")

if (floatNav) {
    floatNav.innerHTML = ""
}

const adCallsBar = document.getElementById("adcalls_bar_player")

if (adCallsBar) {
    adCallsBar.style.display = 'none'
}
