/*
    播放指定直播
    @param pid 直播 id
    @param vid 视频 id
 */
(async function () {
    const live = await initLivePlayer();

    if (live) {
        live.videoConfig.pid = '{{pid}}'
        live.videoConfig.vid = '{{streamId}}'
        console.log('videoConfig: pid=' + live.videoConfig.pid + "vid=" + live.videoConfig.vid)
    }
})();
