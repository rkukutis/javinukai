export default async function ({ entryId, newData }) {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/images/collection/${entryId}`,
    {
      method: "PATCH",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "User-Agent": "react-front",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newData),
    }
  );
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.title);
  }
}
