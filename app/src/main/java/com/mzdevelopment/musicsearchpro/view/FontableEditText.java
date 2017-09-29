package com.mzdevelopment.musicsearchpro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.mzdevelopment.musicsearchpro.R;
import com.mzdevelopment.musicsearchpro.util.UiUtil;

public class FontableEditText extends EditText
{

    public FontableEditText(Context context)
    {
        super(context);
    }

    public FontableEditText(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        UiUtil.setCustomFont(this, context, attributeset, R.styleable.com_sparte_freemusicstream_view_FontableEditText, 0);
    }

    public FontableEditText(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        UiUtil.setCustomFont(this, context, attributeset, R.styleable.com_sparte_freemusicstream_view_FontableEditText, 0);
    }
}
