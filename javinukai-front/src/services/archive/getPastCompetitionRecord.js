export default async function (page, limit, sortBy, sortDesc, contains) {
  const res = await fetch(
    `${
      import.meta.env.VITE_BACKEND
    }/api/v1/archive?page=${page}&limit=${limit}&sortBy=${sortBy}&sortDesc=${sortDesc}${
      contains ? "&contains=" + contains : ""
    }`,
    {
      method: "GET",
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

  return await res.json();
}
