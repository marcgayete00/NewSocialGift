package com.example.newsocialgift;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsocialgift.adapters.ChatAdapter;

public class MessageLayoutManager extends LinearLayoutManager {

    private String currentUserID;
    private RecyclerView recyclerView;

    public MessageLayoutManager(Context context, String currentUserID, RecyclerView recyclerView) {
        super(context);
        this.currentUserID = currentUserID;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getLayoutDirection() {
        return View.LAYOUT_DIRECTION_LTR; // Aseguramos que el layout siempre se muestre de izquierda a derecha
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true; // Habilitamos la medida automática de los elementos
    }

    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);

        // Obtenemos la posición del elemento en la lista
        int position = getPosition(child);

        // Obtenemos el ID del usuario asociado al mensaje en la posición actual
        String messageUserID = getMessageUserID(position);

        // Verificamos si el mensaje es del usuario principal o de otro usuario
        boolean isCurrentUserMessage = currentUserID.equals(messageUserID);

        // Establecemos el ancho y el margen del elemento según sea el caso
        if (isCurrentUserMessage) {
            int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            child.measure(widthSpec, heightSpec);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            int leftMargin = getWidth() - width; // Colocamos el mensaje del usuario principal a la derecha
            int rightMargin = 0;
            setLayoutAlignment(child, leftMargin, rightMargin);
        } else {
            int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            child.measure(widthSpec, heightSpec);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            int leftMargin = 0;
            int rightMargin = getWidth() - width; // Colocamos los mensajes de otros usuarios a la izquierda
            setLayoutAlignment(child, leftMargin, rightMargin);
        }
    }

    private String getMessageUserID(int position) {
        ChatAdapter adapter = getAdapter();
        ChatModel message = adapter.getItem(position);
        return message.getSender();
    }

    private ChatAdapter getAdapter() {
        return (ChatAdapter) recyclerView.getAdapter();
    }

    private void setLayoutAlignment(View child, int leftMargin, int rightMargin) {
        // Establece los márgenes izquierdo y derecho del elemento
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        params.setMargins(leftMargin, params.topMargin, rightMargin, params.bottomMargin);
        child.setLayoutParams(params);
    }
}

