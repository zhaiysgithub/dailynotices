package com.suncity.dailynotices.observer

import com.suncity.dailynotices.utils.LogUtils

class ObserverList<E> : Iterable<E> {

    private val mObservers = arrayListOf<E>()
    private var mIterationDepth = 0
    private var mCount = 0
    private var mNeedsCompact = false

    fun addObserver(obs: E): Boolean {
        return if (obs != null && !this.mObservers.contains(obs)) {
            val result = this.mObservers.add(obs)
            LogUtils.e("addObserver -> $result - $obs")

            ++this.mCount
            true
        } else {
            false
        }
    }

    fun removeObserver(obs: E): Boolean {
        if (obs == null) {
            return false
        } else {
            val index = this.mObservers.indexOf(obs)
            return if (index == -1) {
                false
            } else {
                if (this.mIterationDepth == 0) {
                    this.mObservers.removeAt(index)
                } else {
                    this.mNeedsCompact = true
                }

                --this.mCount
                LogUtils.e("removeObserver -> ${this.mCount >= 0} - $obs")

                true
            }
        }
    }

    fun hasObserver(obs: E): Boolean {
        return this.mObservers.contains(obs)
    }

    fun clear() {
        this.mCount = 0
        if (this.mIterationDepth == 0) {
            this.mObservers.clear()
        } else {
            val size = this.mObservers.size
            this.mNeedsCompact = this.mNeedsCompact or (size != 0)

        }
    }

    override fun iterator(): Iterator<E> {
        return ObserverListIterator()
    }

    fun rewindableIterator(): RewindableIterator<E> {
        return ObserverListIterator()
    }

    fun size(): Int {
        return this.mCount
    }

    fun isEmpty(): Boolean {
        return this.mCount == 0
    }

    private fun compact() {
        if(mObservers.size > 0){
            mObservers.forEach {
                if(it == null){
                    this.mObservers.remove(it)
                }
            }
        }
    }

    private fun incrementIterationDepth() {
        ++this.mIterationDepth
    }

    private fun decrementIterationDepthAndCompactIfNeeded() {
        --this.mIterationDepth

        if (this.mIterationDepth <= 0) {
            if (this.mNeedsCompact) {
                this.mNeedsCompact = false
                this.compact()
            }
        }
    }

    private fun capacity(): Int {
        return this.mObservers.size
    }

    private fun getObserverAt(index: Int): E {
        return this.mObservers[index]
    }

    private inner class ObserverListIterator : RewindableIterator<E>{

        private var mListEndMarker: Int = 0
        private var mIndex: Int = 0
        private var mIsExhausted: Boolean = false

        init {
            this.mIndex = 0
            this.mIsExhausted = false
            incrementIterationDepth()
            this.mListEndMarker = capacity()
        }

        override fun rewind() {
            compactListIfNeeded()
            incrementIterationDepth()
            this.mListEndMarker = capacity()
            this.mIsExhausted = false
            this.mIndex = 0
        }

        override fun hasNext(): Boolean {
            var lookupIndex: Int = this.mIndex
            while (lookupIndex < this.mListEndMarker && this@ObserverList.getObserverAt(lookupIndex) == null) {
                ++lookupIndex
            }
            return if (lookupIndex < this.mListEndMarker) {
                true
            } else {
                compactListIfNeeded()
                false
            }
        }

        override fun next(): E {
            while (this.mIndex < this.mListEndMarker && this@ObserverList.getObserverAt(this.mIndex) == null) {
                ++this.mIndex
            }

            if (this.mIndex < this.mListEndMarker) {
                return getObserverAt(mIndex++)
            } else {
                this.compactListIfNeeded()
                throw NoSuchElementException()
            }
        }

        private fun compactListIfNeeded() {
            if (!this.mIsExhausted) {
                this.mIsExhausted = true
                decrementIterationDepthAndCompactIfNeeded()
            }

        }
    }

    interface RewindableIterator<E> : Iterator<E>{

        fun rewind()
    }
}
