package gg.enes.moderation.bukkit.enums;

import org.bukkit.Material;

public enum ReportConfirm {
    /**
     * Represents the submit button.
     */
    SUBMIT {
        @Override
        public Material getMaterial() {
            return Material.LIME_WOOL;
        }

        @Override
        public String toString() {
            return "submit";
        }
    },
    /**
     * Represents the cancel button.
     */
    CANCEL {
        @Override
        public Material getMaterial() {
            return Material.RED_WOOL;
        }

        @Override
        public String toString() {
            return "cancel";
        }
    };

    /**
     * Returns the material of the report type.
     *
     * @return the material of the report type
     */
    public abstract Material getMaterial();
}
