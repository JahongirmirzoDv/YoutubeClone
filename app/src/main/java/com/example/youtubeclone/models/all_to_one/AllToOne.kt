package com.example.youtubeclone.models.all_to_one

class AllToOne {
    var video_id: String? = null
    var pageToken:String? = null
    var video_image: String? = null
    var video_name: String? = null
    var channel_name: String? = null
    var video_views: String? = null
    var video_date: String? = null
    var channel_image: String? = null
    var channel_id: String? = null

    constructor(
        video_id: String?,
        pageToken: String?,
        video_image: String?,
        video_name: String?,
        channel_name: String?,
        video_views: String?,
        video_date: String?,
        channel_image: String?,
        channel_id: String?
    ) {
        this.video_id = video_id
        this.pageToken = pageToken
        this.video_image = video_image
        this.video_name = video_name
        this.channel_name = channel_name
        this.video_views = video_views
        this.video_date = video_date
        this.channel_image = channel_image
        this.channel_id = channel_id
    }

    constructor()
}