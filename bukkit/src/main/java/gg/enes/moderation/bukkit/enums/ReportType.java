package gg.enes.moderation.bukkit.enums;

import org.bukkit.Material;

public enum ReportType {
    /**
     * Represents a cheating report.
     */
    CHEATING {
        @Override
        public Material getMaterial() {
            return Material.DIAMOND_SWORD;
        }

        @Override
        public String getKey() {
            return "cheating";
        }

        @Override
        public String toString() {
            return "cheating/hacking";
        }
    },
    /**
     * Represents a bad name report.
     */
    BAD_NAME {
        @Override
        public Material getMaterial() {
            return Material.NAME_TAG;
        }

        @Override
        public String getKey() {
            return "bad-name";
        }

        @Override
        public String toString() {
            return "bad name";
        }
    },
    /**
     * Represents a bad skin report.
     */
    BAD_SKIN {
        @Override
        public Material getMaterial() {
            return Material.LEATHER_CHESTPLATE;
        }

        @Override
        public String getKey() {
            return "bad-skin";
        }

        @Override
        public String toString() {
            return "bad skin";
        }
    },
    /**
     * Represents a cross teaming report.
     */
    CROSS_TEAMING {
        @Override
        public Material getMaterial() {
            return Material.IRON_SWORD;
        }

        @Override
        public String getKey() {
            return "cross-teaming";
        }

        @Override
        public String toString() {
            return "cross teaming";
        }
    },
    /**
     * Represents a message report.
     */
    MESSAGE {
        @Override
        public Material getMaterial() {
            return Material.WRITTEN_BOOK;
        }

        @Override
        public String getKey() {
            return "message";
        }

        @Override
        public String toString() {
            return "message";
        }
    },
    /**
     * Represents a public message report.
     */
    PUBLIC_MESSAGE {
        @Override
        public Material getMaterial() {
            return Material.PAPER;
        }

        @Override
        public String getKey() {
            return "public-message";
        }

        @Override
        public String toString() {
            return "public chat";
        }
    },
    /**
     * Represents a private message report.
     */
    PRIVATE_MESSAGE {
        @Override
        public Material getMaterial() {
            return Material.PAPER;
        }

        @Override
        public String getKey() {
            return "private-message";
        }

        @Override
        public String toString() {
            return "direct message";
        }
    },
    /**
     * Represents a back button.
     */
    BACK {
        @Override
        public Material getMaterial() {
            return Material.BARRIER;
        }

        @Override
        public String getKey() {
            return "back";
        }

        @Override
        public String toString() {
            return "back";
        }
    };

    /**
     * Retrieves the material of the report type.
     *
     * @return The material.
     */
    public abstract Material getMaterial();

    /**
     * Retrieves the key of the report type.
     *
     * @return The key.
     */
    public abstract String getKey();
}
