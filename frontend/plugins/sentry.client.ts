import { defineNuxtPlugin } from '#app';
import * as Sentry from '@sentry/browser';
import { Integrations } from '@sentry/tracing';

export default defineNuxtPlugin((nuxtApp) => {
    // const environment = nuxtApp.$config.NODE_ENV;

    Sentry.init({
        dsn: 'https://10a99dc899b14b05a86a7177f92d68e6@o4504552325644288.ingest.sentry.io/4504552326823936',
        environment: process.env.NODE_ENV,
        integrations: [new Integrations.BrowserTracing()],
        sampleRate: 1,
        tracesSampleRate: 1
    });
});
