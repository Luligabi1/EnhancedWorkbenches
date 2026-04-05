package me.luligabi.enhancedworkbenches.common.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class ProjectTableRecipeHistory extends AbstractList<ProjectTableRecipeHistory.RecipeHistoryEntry> {

    public final ArrayList<RecipeHistoryEntry> list = new ArrayList<>(9);


    @Override
    public boolean add(RecipeHistoryEntry entry) {
        RecipeHistoryEntry toggledEntry = new RecipeHistoryEntry(entry.id, !entry.pinned);
        if(list.contains(toggledEntry)) return false;

        if(list.contains(entry)) {
            if(entry.pinned) return false;
            list.remove(entry);
            addToFirstAvailableIndex(entry);
            return true;
        }
        if(list.size() >= MAX_SIZE) {
            if(list.getLast() instanceof RecipeHistoryEntry entry1 && entry1.pinned) return false;
            list.removeLast();
        }
        if(list.isEmpty()) {
            list.add(entry);
        } else {
            addToFirstAvailableIndex(entry);
        }
        return true;
    }

    public void togglePin(int index) {
        if(list.get(index) instanceof RecipeHistoryEntry entry) {
            if(entry.pinned) {
                list.remove(entry);
                entry.pinned = false;
                addToFirstAvailableIndex(entry);
            } else {
                for(int i = 0; i < size(); i++) {
                    if(!list.get(i).pinned) {
                        list.remove(entry);
                        entry.pinned = true;
                        list.add(i, entry);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public RecipeHistoryEntry get(int index) {
        return list.get(index);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public int size() {
        return list.size();
    }

    private void addToFirstAvailableIndex(RecipeHistoryEntry entry) {
        if(list.isEmpty()) {
            list.add(entry);
            return;
        }

        for(int i = 0; i < size(); i++) {
            RecipeHistoryEntry currentEntry = list.get(i);
            if(currentEntry == null || !currentEntry.pinned) {
                list.add(i, entry);
                return;
            }
        }
        list.add(entry);
    }


    public void fromTag(CompoundTag compoundTag) {
        list.clear();

        ListTag listTag = compoundTag.getList("RecipeHistory", 10);
        if(listTag.isEmpty()) return;
        for(int i = 0; i < 9; ++i) { // empty lists can't be set and all that full stuff
            list.add(null);
        }
        for(int i = 0; i < listTag.size(); ++i) {
            CompoundTag entryTag = listTag.getCompound(i);
            int index = entryTag.getByte("Index") & 255;
            ResourceLocation id = ResourceLocation.parse(entryTag.getString("Id"));
            boolean pinned = entryTag.getBoolean("Pinned");
            RecipeHistoryEntry entry = new RecipeHistoryEntry(id, pinned);
            list.set(index, entry);
        }
    }

    public CompoundTag toTag(CompoundTag tag) {
        ListTag historyList = new ListTag();
        for(int i = 0; i < size(); ++i) {
            RecipeHistoryEntry entry = list.get(i);
            if(entry == null) continue;

            CompoundTag entryTag = new CompoundTag();
            entryTag.putByte("Index", (byte) i);
            entryTag.putString("Id", entry.id.toString());
            entryTag.putBoolean("Pinned", entry.pinned);
            historyList.add(entryTag);
        }

        tag.put("RecipeHistory", historyList);
        return tag;
    }

    private static final int MAX_SIZE = 9;

    public static class RecipeHistoryEntry {

        protected final ResourceLocation id;
        protected boolean pinned;

        public RecipeHistoryEntry(ResourceLocation id) {
            this(id, false);
        }

        public RecipeHistoryEntry(ResourceLocation id, boolean pinned) {
            this.id = id;
            this.pinned = pinned;
        }

        public RecipeHolder<?> toRecipeHolder(Level level) {
            Optional<RecipeHolder<?>> recipe = level.getRecipeManager().byKey(id);
            if(recipe.isEmpty()) throw new IllegalStateException("Found invalid recipe on RecipeHistoryEntry: " + id.toString());
            return recipe.get();
        }

        public ResourceLocation getId() {
            return id;
        }

        public boolean isPinned() {
            return pinned;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            RecipeHistoryEntry entry = (RecipeHistoryEntry) o;
            return pinned == entry.pinned && Objects.equals(id, entry.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, pinned);
        }
    }
}