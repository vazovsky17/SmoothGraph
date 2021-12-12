package app.vazovsky.smoothgraph

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.vazovsky.smoothgraph.databinding.ActivityMainBinding
import app.vazovsky.smoothgraph.model.Point
import by.kirich1409.viewbindingdelegate.viewBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = mutableListOf<Point>()
        list.add(Point("lol", 10F))
        list.add(Point("lol", 20F))
        list.add(Point("lol", 50F))
        list.add(Point("lol", 10F))
        list.add(Point("lol", 70F))
        list.add(Point("lol", 10F))
        list.add(Point("lol", 50F))
        list.add(Point("lol", 10F))
        list.add(Point("lol", 0F))
        list.add(Point("lol", 70F))
        list.add(Point("lol", 10F))

        binding.smoothGraphView.apply {
            setData(list)
            startAnim()
        }
    }
}