package com.example.pablo384.newsapp.Extensions

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by pablo384 on 16/12/17.
 */
operator fun JSONArray.iterator():Iterator<JSONObject>
        =(0 until length()).asSequence().map { get(it) as JSONObject }.iterator()