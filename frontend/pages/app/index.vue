<template>
    <div class="h-full overflow-auto flex flex-col">
        <div class="flex sticky-header flex-row gap-8 p-8">
            <div class="my-auto flex">
                <div class="indicator">
                    <!--
                    <div class="indicator-item indicator-bottom">
                        <div class="dropdown">
                            <label
                                tabindex="0"
                                class="btn btn-circle btn-xs m-1 bg-base-100 hover:bg-base-200 border-0"
                            >
                                <ChevronDownIcon class="h-3 w-3 stroke-[3px] text-primary"/>
                            </label>
                            <ul
                                tabindex="0"
                                class="dropdown-content menu p-2 shadow bg-base-100 rounded-box w-52 "
                            >
                                <li><a>Item 1</a></li>
                                <li><a>Item 2</a></li>
                            </ul>
                        </div>
                    </div>
                    -->
                    <Avatar :ring="true" :user="currentUser" v-if="currentUser" />
                </div>
            </div>
            <h2 class="font-bold text-3xl text-center text-primary w-full align-middle my-auto">Left Hand</h2>
            <div class="my-auto flex gap-4">
                <button class="btn btn-circle btn-primary">
                    <QrCodeIcon class="h-6 w-6 stroke-2"/>
                </button>
                <button class="btn btn-circle btn-primary">
                    <CameraIcon class="h-6 w-6 stroke-2"/>
                </button>
            </div>
        </div>

        <div class="p-8 bg-secondary text-secondary-content">
            <h2 class="font-bold text-xl flex flex-row gap-2 mb-2">
                <ViewfinderCircleIcon class="w-5 h-5 stroke-2 my-auto" />
                Your Target
            </h2>
            <div class="text-center flex flex-col items-center" v-if="target">
                <Avatar :user="target" large />
                <span class="font-bold text-lg">{{ target.forename }} {{ target.surname }}</span>
                <span class="text-sm"><!-- #4 • -->{{ target.eliminated.length }} kills</span>
            </div>
        </div>
        <div class="p-8 flex flex-col gap-4">
            <KillFeedBlock v-for="n in 10" :key="n"/>
        </div>
    </div>
</template>

<script lang="ts">
import type {PropType} from 'vue';
import type {EliminationUser} from '@/utils/types';
import {ChevronDownIcon, QrCodeIcon, CameraIcon, ViewfinderCircleIcon} from "@heroicons/vue/24/outline/index.js";

export default {
    components: {ChevronDownIcon, QrCodeIcon, CameraIcon, ViewfinderCircleIcon},
    props: {
        currentUser: Object as PropType<EliminationUser>,
        target: Object as PropType<EliminationUser>
    }
};
</script>
