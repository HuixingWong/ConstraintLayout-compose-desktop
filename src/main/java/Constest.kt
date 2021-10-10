import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet

@Composable
fun ConstraintTest() {
    Box(modifier = Modifier.size(2000.dp)) {
        ConstraintLayout(
            constraintSet = constraintSets(),
            modifier = Modifier.background(Color.Gray).size(1000.dp)
        ) {
            Box(modifier = Modifier.background(Color.Red).size(100.dp).layoutId(Ref.A))
            Box(modifier = Modifier.background(Color.Red).size(100.dp).layoutId(Ref.B))
            Box(modifier = Modifier.background(Color.Red).size(100.dp).layoutId(Ref.C))
            Box(modifier = Modifier.background(Color.Red).size(100.dp).layoutId(Ref.D))
        }
    }

}


@Composable
fun constraintSets() = ConstraintSet {
    val A = createRefFor(Ref.A)
    val B = createRefFor(Ref.B)
    val C = createRefFor(Ref.C)
    val D = createRefFor(Ref.D)
    constrain(A) {
//        top.linkTo(parent.top)
        start.linkTo(parent.start)
    }
    constrain(B) {
        top.linkTo(A.bottom)
        start.linkTo(A.end)
    }
    constrain(C) {
        top.linkTo(B.bottom)
        start.linkTo(B.end)
    }
    constrain(D) {
        top.linkTo(C.bottom)
        start.linkTo(C.end)
    }
}

object Ref {
    const val A = "AAAAA"
    const val B = "BBBBB"
    const val C = "CCCCC"
    const val D = "DDDDD"
}