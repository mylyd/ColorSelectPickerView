目前只支持矩形样式（包含圆角矩形）的选色器

![预览图](./preview.png?width=175&height=384)



#### 使用

- 将Module中`color_picker`放置到项目中，如果需要自行排版可以修改`view_color_picker.xml`文件

- 具体使用：

  ``` xml
  </.../>
  <com.select.color_picker.ColorSelectView
     android:id="@+id/color_picker"
     android:layout_width="match_parent"
     android:layout_height="wrap_content" />
  </.../>
  ```

  ``` kotlin
  binding.colorPicker.colorChanged = {
      //it就是选中的色值 （ColorInt）    
  }
  ```

  

