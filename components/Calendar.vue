<template>
    <div class="grid grid-cols-7 rounded-lg overflow-hidden text-center border border-gray-300/10">
        <div v-for="day in ['S', 'M', 'T', 'W', 'T', 'F', 'S']" :key="day" class="bg-base-200 py-2 px-4">
            {{ day }}
        </div>

        <!-- TODO: make this data into an object, map -->
        <!-- TODO: time system, highlight current day -->
        <!-- TODO: mobile display -->
        <CalendarCell date="22" />
        <CalendarCell date="23" />
        <CalendarCell date="24" />
        <CalendarCell date="25" bold>
            Plushie Handouts @ Senior Quad, Lunch, Afterschool
        </CalendarCell>
        <CalendarCell date="26" bold>
            Plushie Handouts @ Senior Quad, Lunch, Afterschool
        </CalendarCell>
        <CalendarCell date="27">
            <strong>GAME STARTS 12 AM:</strong> <em>To stay safe,</em> players must hold the animal with their right hand.
        </CalendarCell>
        <CalendarCell date="28" />

        <CalendarCell date="29" bold>
            On weekends, players must abide by all rules and must carry a plushie to be safe.
        </CalendarCell>
        <CalendarCell date="30">
            <em>To stay safe,</em> players must be holding the animal with both hands.
        </CalendarCell>
        <CalendarCell date="31">
            <em>To stay safe,</em> players must be under a roof or overhang.
        </CalendarCell>
        <CalendarCell date="1">
            <strong>Targets Change 8 PM:</strong> <em>To stay safe,</em> players must be touching a wall.
        </CalendarCell>
        <CalendarCell date="2">
            <em>To stay safe,</em> players must be holding their animal <strong>ABOVE</strong> their shoulder.
        </CalendarCell>
        <CalendarCell date="3">
            <em>To stay safe,</em> players must be holding their animal <strong>BELOW</strong> their waist.
        </CalendarCell>
        <CalendarCell date="4" />

        <CalendarCell date="5" bold>
            For this week, nobody is safe: there are only requirements for eliminating others.
        </CalendarCell>
        <CalendarCell date="6">
            <em>To eliminate others,</em> players must be (visibly) wearing <strong>RED</strong>.
        </CalendarCell>
        <CalendarCell date="7">
            <em>To eliminate others,</em> players must wear a <strong>HAT</strong>.
        </CalendarCell>
        <CalendarCell date="8">
            <strong>Targets Change 8 PM:</strong> <em>To eliminate others,</em> players must be wearing <strong>JEANS</strong>.
        </CalendarCell>
        <CalendarCell date="9">
            <em>To eliminate others,</em> players must be <strong>SITTING DOWN</strong>.
        </CalendarCell>
        <CalendarCell date="10">
            <strong>NO ONE IS SAFE</strong> (anyone can be tagged anytime even with animal).
        </CalendarCell>
        <CalendarCell date="11" />
    </div>
</template>

<script>
export default {
    name: "Calendar"
}
</script>
