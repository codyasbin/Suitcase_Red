package com.example.suitcase2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private DatabaseHelper myDbHelper;
    private static final int PICK_CONTACT_REQUEST = 2;
    private Item selectedItem; // Track selected item

    public ItemAdapter(Context context, List<Item> itemList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.itemList = itemList;
        this.myDbHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText("Price: " + item.getPrice());
        holder.itemDescription.setText(item.getDescription());

        // Load image asynchronously from byte array
        if (item.getImage() != null && item.getImage().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
            holder.itemImage.setImageBitmap(bitmap);
        } else {
            holder.itemImage.setImageResource(R.drawable.ic_launcher_foreground); // Placeholder image if no image is available
        }

        // Delegate button click listener
        holder.btnDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    selectedItem = itemList.get(adapterPosition); // Track selected item

                    // Start contact picker activity
                    Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    ((MainActivity) context).startActivityForResult(intent, PICK_CONTACT_REQUEST);
                }
            }
        });

        // Edit button click listener
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Item itemToEdit = itemList.get(adapterPosition);

                    // Create an intent to navigate to EditItemActivity
                    Intent intent = new Intent(context, EditItemActivity.class);
                    intent.putExtra("ITEM_ID", itemToEdit.getId());
                    context.startActivity(intent);
                }
            }
        });

        // Delete button click listener
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Item deletedItem = itemList.get(adapterPosition);

                    // Delete item from database using its ID
                    myDbHelper.deleteItem(deletedItem.getId());

                    // Remove item from the list
                    itemList.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(adapterPosition, getItemCount());

                    // Show toast message
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Mark as purchased button click listener
        holder.btnMarkAsPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Item itemToMark = itemList.get(adapterPosition);

                    // Update item status in database
                    boolean isPurchased = !itemToMark.isPurchased();
                    myDbHelper.markItemAsPurchased(itemToMark.getId(), isPurchased);

                    // Update item in the list and notify adapter
                    itemToMark.setPurchased(isPurchased);
                    notifyItemChanged(adapterPosition);

                    // Show toast message
                    Toast.makeText(context, isPurchased ? "Item marked as purchased" : "Item marked as not purchased", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Geotag button click listener
        holder.Geotag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open MapActivity to view the current location
                Intent intent = new Intent(context, MapActivity.class);
                context.startActivity(intent);
            }
        });


        // Update the button text based on purchase status
        if (item.isPurchased()) {
            holder.btnMarkAsPurchased.setText("Purchased");
            holder.btnMarkAsPurchased.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        } else {
            holder.btnMarkAsPurchased.setText("Mark as Purchased");
            holder.btnMarkAsPurchased.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Method to get the selected item
    public Item getSelectedItem() {
        return selectedItem;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        TextView itemDescription;
        ImageView itemImage;
        ImageView btnEdit;
        ImageView btnDelete;
        Button btnMarkAsPurchased;
        ImageView Geotag;              ; // Button for geotagging
        ImageView btnDelegate;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemDescription = itemView.findViewById(R.id.item_description);
            itemImage = itemView.findViewById(R.id.item_image);
            btnEdit = itemView.findViewById(R.id.edit_button);
            btnDelete = itemView.findViewById(R.id.delete_button);
            btnMarkAsPurchased = itemView.findViewById(R.id.mark_as_purchased_button);
           Geotag = itemView.findViewById(R.id.view_location_button); // Initialize geotag button
            btnDelegate = itemView.findViewById(R.id.delegate_button);

            // Implement swipe-to-delete gesture
            itemView.setOnTouchListener(new OnSwipeTouchListener(context) {
                @Override
                public void onSwipeRight() {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Item deletedItem = itemList.get(position);

                        // Delete item from database using its ID
                        myDbHelper.deleteItem(deletedItem.getId());

                        // Remove item from the list
                        itemList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());

                        // Show toast message
                        Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
