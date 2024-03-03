export default async function ({ data, t }) {
  const res = await fetch(`${import.meta.env.VITE_BACKEND}/api/v1/images`, {
    method: "POST",
    mode: "cors",
    cache: "no-cache",
    credentials: "include",
    body: data,
  });
  if (!res.ok) {
    throw new Error(t("services.uploadImagesError"));
  }
  return await res.json();
}
