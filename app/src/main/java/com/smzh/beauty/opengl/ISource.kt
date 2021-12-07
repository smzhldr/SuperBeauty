package com.smzh.beauty.opengl

interface ISource {
    fun createFrame(): IFilter.Frame?
    fun destroy()
    val width: Int
    val height: Int
}