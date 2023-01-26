<template>
    <label for="code-modal" class="btn btn-circle btn-primary">
        <QrCodeIcon class="h-6 w-6 stroke-2"/>
    </label>
</template>

<script lang="ts">
import {QrCodeIcon} from "@heroicons/vue/24/outline/index.js";

export default {
    name: "KillCodeButton",
    components: {QrCodeIcon}
}
</script>
