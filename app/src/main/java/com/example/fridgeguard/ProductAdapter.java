package com.example.fridgeguard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnDeleteClickListener onDeleteClickListener;

    public ProductAdapter(List<Product> productList, OnDeleteClickListener onDeleteClickListener) {
        this.productList = productList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void deleteItem(int position) {
        Product product = productList.get(position);
        productList.remove(position);
        notifyItemRemoved(position);
        onDeleteClickListener.onDeleteClick(product);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;
        private TextView productRemainingDays;
        private Button deleteButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productRemainingDays = itemView.findViewById(R.id.product_remaining_days);
            deleteButton = itemView.findViewById(R.id.delete_button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        deleteItem(position);
                    }
                }
            });
        }

        public void bind(Product product) {
            productName.setText(product.getName());
            String remainingDaysText = product.getRemainingDays() + " days";
            productRemainingDays.setText(remainingDaysText);
            byte[] imageData = product.getImageData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            productImage.setImageBitmap(bitmap);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Product product);
    }
}
