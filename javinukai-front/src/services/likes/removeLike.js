export default async function (imageCollectionId) {
  const res = await fetch(
    `${
      import.meta.env.VITE_BACKEND
    }/api/v1/removeLike?collectionId=${imageCollectionId}`,
    {
      method: "PATCH",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "User-Agent": "react-front",
      },
    }
  );
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.title);
  }
}
