package noted.noted;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import noted.noted.Models.Contact;
import noted.noted.Models.Model;
import noted.noted.Models.Note;

public class GridAdapter extends BaseAdapter {

    List<Note> lNotes = new ArrayList<Note>();
    Context context;
    boolean isReceived;

    public GridAdapter(Context context, boolean isReceived) {
        this.context = context;
        this.isReceived = isReceived;
        Log.d("GridAdapter", "isReceived" + isReceived);
        if (this.isReceived) {
            Log.d("GridAdapter", "Received ");
            Model.getInstance().getReceivedLocalNotesAsync(new Model.GetNotesListener() {
                                                               @Override
                                                               public void onResult(List<Note> notes) {
                                                                   if (notes.size() > 0) {
                                                                       for (Note note : notes) {
                                                                           Log.d("GridAdapter", "addedReceived " + note.getId());
                                                                           lNotes.add(note);
                                                                       }
                                                                       notifyDataSetChanged();
                                                                   }
                                                               }
                                                           },
                    Model.getInstance().getCurrUser().getPhoneNumber(), true);
        } else {
            Log.d("GridAdapter", "sent");
            Model.getInstance().getSentLocalNotesAsync(new Model.GetNotesListener() {
                                                           @Override
                                                           public void onResult(List<Note> notes) {
                                                               if (notes.size() > 0) {
                                                                   for (Note note : notes) {
                                                                       Log.d("GridAdapter", "addedSent " + note.getId());
                                                                       lNotes.add(note);
                                                                   }
                                                                   notifyDataSetChanged();
                                                               }
                                                           }
                                                       },
                    Model.getInstance().getCurrUser().getPhoneNumber());
        }
/*
        Model.getInstance().getAllLocalNotesAsync(new Model.GetNotesListener() {
            @Override
            public void onResult(List<Note> notes) {
                if (notes.size() > 0) {
                    for (Note note : notes) {
                        lNotes.add(note);
                    }
                    notifyDataSetChanged();
                }
            }
        });*/
    }

    @Override
    public int getCount() {
        return lNotes.size();
    }

    @Override
    public Note getItem(int position) {
        return lNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lNotes.get(position).getNumericId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_note_item, null);
        }

        TextView noteContact = (TextView) convertView.findViewById(R.id.noteContact);
        TextView details = (TextView) convertView.findViewById(R.id.noteDetails);

        Note note = getItem(position);
        Contact contact;
        String show;
        if (isReceived) {
            contact = Model.getInstance().getContact(note.getFrom());
            show = note.getFrom();
            if (contact != null) {
                show = contact.getName();
            }
            noteContact.setText("FROM " + show);
        } else {
            contact = Model.getInstance().getContact(note.getTo());
            show = note.getTo();
            if (contact != null) {
                show = contact.getName();
            }
            noteContact.setText("TO " + show);
        }
        details.setText(note.getDetails() + "");

        return convertView;
    }


}