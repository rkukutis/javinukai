import { useTranslation } from "react-i18next";

export default async function (formData) {
  const { t } = useTranslation();
  const res = await fetch(`${import.meta.env.VITE_BACKEND}/api/v1/images`, {
    method: "POST",
    mode: "cors",
    cache: "no-cache",
    credentials: "include",
    body: formData,
  });
  if (!res.ok) {
    throw new Error(t('services.uploadImagesError'));
  }
  return await res.json();
}
