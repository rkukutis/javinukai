import i18next from "i18next";
import Backend from "i18next-http-backend";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";

i18next.use(Backend).use(initReactI18next).use(LanguageDetector).init({
  fallbackLng: "en",
  debug: false,
});

export default i18next;
