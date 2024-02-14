export default async function (formData) {
  console.log(formData);
  const res = await fetch("http://localhost:8080/api/v1/images", {
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
