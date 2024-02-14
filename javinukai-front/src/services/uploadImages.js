export default async function (formData) {
  const res = await fetch(`${import.meta.env.VITE_BACKEND}/api/v1/images`, {
    method: "POST",
    mode: "cors",
    cache: "no-cache",
    credentials: "include",
    body: formData,
  });
  if (!res.ok) {
    throw new Error("Could not upload images");
  }
  return await res.json();
}
