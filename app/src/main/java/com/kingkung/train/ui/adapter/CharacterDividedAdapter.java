package com.kingkung.train.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingkung.train.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class CharacterDividedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_CHARACTER = 0;

    private int mSize;
    private SortedSet<ItemGroup> mGroups = new TreeSet<>((o1, o2) -> o1.mSortCharacter - o2.mSortCharacter);

    public abstract static class CharacterItem {
        public static final int INVALID_POSITION = -1;

        private int mPosition = INVALID_POSITION;

        private final void setPosition(int position) {
            mPosition = position;
        }

        public final int getPosition() {
            return mPosition;
        }

        public abstract String getCharacter();

        public abstract char getSortCharacter();

//        public abstract long getStableId();
    }

    private static class CharacterViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public CharacterViewHolder(View view) {
            super(view);
            if (view instanceof TextView) {
                mTextView = (TextView) view;
            }
        }

        public void setCharacter(String character) {
            mTextView.setText(character);
        }
    }

    private static class ItemGroup {
        private final String mCharacter;
        private final char mSortCharacter;
        private final List<CharacterItem> mItems = new ArrayList<>();

        private int mIndex;

        private boolean mIsSorted;

        public ItemGroup(CharacterItem item) {
            mCharacter = item.getCharacter();
            mSortCharacter = item.getSortCharacter();
            mItems.add(item);
            mIsSorted = true;
        }

        public void addItem(CharacterItem item) {
            mItems.add(item);
            mIsSorted = false;
        }

        public void setPosition(int index) {
            assert mIndex == 0;
            mIndex = index;

            sortIfNeeded();
            for (CharacterItem item : mItems) {
                index += 1;
                item.setPosition(index);
            }
        }

        public void resetPosition() {
            mIndex = CharacterItem.INVALID_POSITION;
            for (CharacterItem item : mItems) {
                item.setPosition(CharacterItem.INVALID_POSITION);
            }
        }

        public boolean isSameCharacter(String otherCharacter) {
            return mCharacter.equals(otherCharacter);
        }

        public int size() {
            return mItems.size() + 1;
        }

        public CharacterItem getItemAt(int index) {
            if (index == 0) return null;

            sortIfNeeded();
            return mItems.get(index - 1);
        }

        private void sortIfNeeded() {
            if (mIsSorted) return;
            mIsSorted = true;

            Collections.sort(mItems, (lhs, rhs) -> lhs.getSortCharacter() - rhs.getSortCharacter());
        }
    }

    @Override
    public final int getItemViewType(int position) {
        Pair<ItemGroup, Integer> pair = getGroupAt(position);
        return pair.second == 0 ? TYPE_CHARACTER : getItemViewTypeForCharacterItem(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CHARACTER) {
            return new CharacterViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_character, parent, false));
        }
        return createViewHolderForCharacterItem(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Pair<String, CharacterItem> pair = getItemAt(position);
        if (viewHolder instanceof CharacterViewHolder) {
            ((CharacterViewHolder) viewHolder).setCharacter(pair.first);
        } else {
            bindViewHolderForCharacterItem(viewHolder, pair.second);
        }
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    public Pair<String, CharacterItem> getItemAt(int position) {
        Pair<ItemGroup, Integer> pair = getGroupAt(position);
        ItemGroup group = pair.first;
        return new Pair<>(group.mCharacter, group.getItemAt(pair.second));
    }

    private Pair<ItemGroup, Integer> getGroupAt(int position) {
        int i = position;
        for (ItemGroup group : mGroups) {
            if (i >= group.size()) {
                i -= group.size();
            } else {
                return new Pair<>(group, i);
            }
        }
        assert false;
        return null;
    }

    public void loadItems(List<? extends CharacterItem> characterItems) {
        mSize = 0;

        for (ItemGroup group : mGroups) {
            group.resetPosition();
        }
        mGroups.clear();

        for (CharacterItem characterItem : characterItems) {
            String character = characterItem.getCharacter();
            boolean found = false;
            for (ItemGroup group : mGroups) {
                if (group.isSameCharacter(character)) {
                    found = true;
                    group.addItem(characterItem);
                    mSize++;
                    break;
                }
            }
            if (!found) {
                mGroups.add(new ItemGroup(characterItem));
                mSize += 2;
            }
        }

        int startIndex = 0;
        for (ItemGroup group : mGroups) {
            group.setPosition(startIndex);
            startIndex += group.size();
        }

        notifyDataSetChanged();
    }

    public void addItems(List<? extends CharacterItem> characterItems) {
        for (CharacterItem characterItem : characterItems) {
            String character = characterItem.getCharacter();
            boolean found = false;
            for (ItemGroup group : mGroups) {
                if (group.isSameCharacter(character)) {
                    found = true;
                    group.addItem(characterItem);
                    mSize++;
                    break;
                }
            }
            if (!found) {
                mGroups.add(new ItemGroup(characterItem));
                mSize += 2;
            }
        }

        int startIndex = 0;
        for (ItemGroup group : mGroups) {
            group.setPosition(startIndex);
            startIndex += group.size();
        }

        notifyDataSetChanged();
    }

    public void addGroup(CharacterItem characterItem) {
        mGroups.add(new ItemGroup(characterItem));
        mSize += 2;

        int startIndex = 0;
        for (ItemGroup group : mGroups) {
            group.setPosition(startIndex);
            startIndex += group.size();
        }

        notifyDataSetChanged();
    }

    public int getPosition(String character) {
        Iterator<ItemGroup> it = mGroups.iterator();
        int position = 0;
        while (it.hasNext()) {
            ItemGroup itemGroup = it.next();
            if (itemGroup.mCharacter.equalsIgnoreCase(character)) {
                position = itemGroup.mIndex;
                break;
            }
        }
        return position;
    }

    protected abstract int getItemViewTypeForCharacterItem(int position);

    protected abstract RecyclerView.ViewHolder createViewHolderForCharacterItem(ViewGroup parent, int viewType);

    protected abstract void bindViewHolderForCharacterItem(RecyclerView.ViewHolder viewHolder, CharacterItem item);
}
