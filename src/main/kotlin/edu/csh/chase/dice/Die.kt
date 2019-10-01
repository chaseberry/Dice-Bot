package edu.csh.chase.dice

class Die(val max: Int) {

    fun roll(): Int {
        return (Math.random() * max).toInt() + 1
    }

}
