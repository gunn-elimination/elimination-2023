<template>
    <div class="grid grid-cols-7 rounded-lg overflow-hidden text-center border border-gray-300/10">
        <div v-for="day in ['S', 'M', 'T', 'W', 'T', 'F', 'S']" :key="day" class="bg-base-300 py-2 px-4">
            {{ day }}
        </div>

        <!-- TODO: mobile display -->
        <CalendarCell
            v-for="[date, item] in Object.entries(calendar)"
            :date="DateTime.fromISO(date)"
            :item="item"
        />
    </div>
</template>

<script lang="ts">
export default {
    name: "Calendar"
}
</script>

<script setup lang="ts">
import {DateTime} from 'luxon';
import {calendar} from '@/utils/calendar';
</script>
