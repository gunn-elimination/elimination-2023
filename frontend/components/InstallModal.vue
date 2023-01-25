<template>
    <input type="checkbox" id="install-modal" class="modal-toggle" :checked="checked" />

    <label for="install-modal" class="modal modal-bottom sm:modal-middle cursor-pointer bg-black/40">
        <label class="modal-box overflow-visible rounded-t-lg sm:rounded-lg" for="">
            <img
                src="/icon.png"
                alt="Home screen icon"
                class="absolute rounded-3xl h-24 w-24 -top-10 left-0 right-0 mx-auto"
            />
            <h3 class="text-lg font-semibold mt-9">
                It looks like you're playing elimination in the iOS Safari app.
            </h3>
            <hr class="border-secondary my-3" />
            <section class="overflow-scroll scroll-smooth scrollbar-none pb-6">
                <p class="mb-3">Elimination is best experienced when installed as a PWA.</p>
                <p class="mb-3">To add elimination to your home screen,</p>
                <ol class="list-decimal list-inside space-y-1 mb-3">
                    <li>Tap the share button <ArrowUpOnSquareIcon class="inline h-6 w-6 p-1 bg-base-200 rounded-md" />.</li>
                    <li>Select <strong>"Add to Home Screen"</strong> in the bottom row.</li>
                    <div class="p-2 bg-base-200 rounded-lg">
                        <span class="flex items-center justify-between text-sm font-light p-3 bg-base-100 rounded-lg">
                            Add to Home Screen
                            <!-- TODO: there's no plus square icon, but the iOS icon is a rounded square -->
                            <PlusCircleIcon class="h-[1.1rem] w-[1.1rem]" />
                        </span>
                    </div>
                    <li>In the dialogue, tap <strong>"Add"</strong>.</li>
                </ol>
                <p class="text-secondary text-sm">
                    Not interested?
                    <button class="font-semibold focus:outline-none focus:underline" @click="() => checked = false">
                        Dismiss.
                    </button>
                </p>
            </section>
        </label>
    </label>
</template>

<script lang="ts">
import { ArrowUpOnSquareIcon, PlusCircleIcon } from "@heroicons/vue/24/outline/index.js";

export default {
    components: {
        ArrowUpOnSquareIcon,
        PlusCircleIcon
    },
};
</script>

<script setup lang="ts">
// TODO: find out how to make this transition on render
// @ts-ignore
const checked = ref((/iPad|iPod|iPhone/).test(navigator.userAgent) && !navigator.standalone);
</script>
