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

    ```xml
    <declare-styleable name="RainbowColorView">
          <!--选色卡，明暗度View的圆角值，目前仅支持矩形样式（包含正方形），不支持圆形，如果不需要圆角，给0dp即可-->
          <attr name="round" format="dimension" />
          <!--必选项，一般view_color_picker.xml文件中已经内置好了，不同的type会对应不同View的绘制，不选择默认彩虹条-->
          <attr name="graph_type" format="enum">
              <!--彩虹色条-->
              <enum name="color_strip" value="1" />
              <!--透明度控制条-->
              <enum name="alpha_strip" value="2" />
              <!--预览-->
              <enum name="preview_strip" value="3" />
              <!--明暗度色板-->
              <enum name="color_board" value="4" />
          </attr>
          <!--预览是否是圆形，默认是矩形，仅在设 graph_type = preview_strip 时生效 ； 且圆形的直径大小取决于高度，不会取决于宽度-->
          <attr name="preview_circle" format="boolean" />
          <!--明暗度色板 是否是正方形 ，以宽度为标准-->
          <attr name="is_square" format="boolean"/>
      </declare-styleable>
    ```

  

