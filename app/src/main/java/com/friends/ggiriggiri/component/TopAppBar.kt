package com.friends.ggiriggiri.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title:String = "",
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIconImage:ImageVector? = null,
    navigationIconOnClick:() -> Unit = {},
    menuItems : @Composable RowScope.() -> Unit = {},
    isDivider : Boolean? = true
) {
    Column {
        CenterAlignedTopAppBar(
            // 타이틀
            title = {
                Text(
                    text = title,
                    fontFamily = FontFamily(Font(R.font.nanumsquareextrabold))
                )
            },
            // 네비게이션 아이콘
            navigationIcon = if (navigationIconImage == null) {
                {}
            } else {
                {
                    IconButton(
                        onClick = navigationIconOnClick
                    ) {
                        Icon(
                            imageVector = navigationIconImage,
                            contentDescription = null
                        )
                    }
                }
            },
            actions = {
                menuItems()
            },
            scrollBehavior = scrollBehavior
        )
        if (isDivider == true){
            Divider(
                color = Color.Gray,
                thickness = 1.dp
            )
        }
    }
}