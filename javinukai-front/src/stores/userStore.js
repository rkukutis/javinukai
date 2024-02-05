import { create } from "zustand";
import { persist } from "zustand/middleware";

const useUserStore = create(
  persist(
    (set, get) => ({
      user: undefined,
      setUser: (user) => set(() => ({ user: user })),
      removeUser: () => set({ user: {} }),
    }),
    { name: "userStorage" }
  )
);

export default useUserStore;
