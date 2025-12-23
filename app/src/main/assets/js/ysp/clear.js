function clear() {
    const player = document.querySelector(".tv-main-con-l-vid")
    if (player) {
        console.log('player:' + player)
        player.style.position = 'fixed'
        player.style.left = 0
        player.style.top = 0
        player.style.width = '100vw'
        player.style.height = '100vh'
        player.style.background = '#000000'

        const content = document.querySelector(".comPadding")

        if (content) {
            content.style.padding = 0
            content.style.width = '100vw'
            content.style.height = '100vh'
            content.style.background = '#000000'
        }

        const tv = document.querySelector(".tv")

        if (tv) {
            tv.style.padding = 0
            tv.style.width = '100vw'
            tv.style.height = '100vh'
            tv.style.background = '#000000'
        }

        const tvZhan = document.querySelector('.tv-zhan')

        if (tvZhan) {
            tvZhan.style.display = 'none'
        }

        const footer = document.querySelector('.max-footer')

        if (footer) {
            footer.style.display = 'none'
        }

        const mainColR = document.querySelector(".tv-main-con-r")

        if (mainColR) {
            mainColR.style.display = 'none'
        }
        return
    }
    setTimeout(function() {
        clear()
    }, 16)
}

clear()

function clearHeader() {
    const header = document.querySelector('.header-fixed')

    if (header) {
        header.style.display = 'none'
        return
    }

    setTimeout(function() {
        clearHeader()
    }, 10)
}


clearHeader()