import { defineNuxtPlugin } from '#app';
import * as Sentry from '@sentry/browser';
import { Integrations } from '@sentry/tracing';

export default defineNuxtPlugin((nuxtApp) => {
    // const environment = nuxtApp.$config.NODE_ENV;

    Sentry.init({
        dsn: 'https://a33dc5477849434fa1c892d37879138f@o4504552325644288.ingest.sentry.io/4504552326823937',
        environment: process.env.NODE_ENV,
        integrations: [new Integrations.BrowserTracing()],
        sampleRate: 1,
        tracesSampleRate: 1
    });
});
