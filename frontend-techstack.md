# JAVIS AI - Frontend Tech Stack

## Core Framework

| Library | Version | Status | Note |
|---------|---------|--------|------|
| React | 19.x | **Latest** | 19.2.3 (Dec 2025), có Activity API, useEffectEvent |
| Vite | 7.x | **Latest** | 7.3.1, requires Node.js 20.19+ |
| TypeScript | 5.x | Stable | Type safety |

> **Note về React 19:**
> - React 19.2 stable, recommended cho new projects
> - Có `<Activity>` API mới (hide/restore UI state)
> - `useEffectEvent` hook mới
> - Requires Node.js 20+

> **Note về Vite:**
> - Vite 8 beta đã có (Rolldown-powered) nhưng chưa stable
> - Dùng **Vite 7.x** cho production

---

## UI Library

| Library | Version | Status | Note |
|---------|---------|--------|------|
| Mantine | 8.x | **Latest** | 8.3.13 (Sep 2025), major redesign |

> **Note về Mantine 8:**
> - v8.0.0 release May 2025
> - Visual redesign, performance improvements
> - Code-highlight package refactored (shiki/highlight.js adapters)
> - Tiptap 3 support

### Mantine Components cần Customize

| Component | Priority | Lý do |
|-----------|----------|-------|
| Table | High | Chiếm nhiều UI, row/header styling |
| Button | High | Brand colors, hover states |
| AppShell + NavLink | High | Sidebar layout, first impression |
| Badge | High | Status colors (pending/processing/done/error) |
| Card | Medium | Dashboard stats, assistant cards |
| Modal | Medium | Confirm dialogs, forms |
| Dropzone | Medium | Document upload styling |
| Tabs | Medium | Assistant detail view |
| TextInput/Select | Low | Form fields, minimal custom |

### Customization Strategy

1. **Theme Override** (Global)
   - Primary color: Brand color
   - Font family: Custom font (Inter/Plus Jakarta Sans)
   - Border radius: Consistent
   - Shadows: Subtle

2. **Styles API** (Component-level)
   - Mantine cung cấp documented style selectors
   - Dùng `styles` prop hoặc `classNames`

3. **CSS Modules** (Page-specific)
   - Cho layout và styles riêng từng page

---

## Routing

| Library | Version | Status | Note |
|---------|---------|--------|------|
| React Router | 7.x | **Latest** | 7.12.0, RSC support (experimental) |

> **Note về React Router 7:**
> - Requires Node.js 20, React 18+
> - No breaking changes nếu đã enable future flags
> - Lazy Route Discovery feature
> - Upgrade path từ v6 non-breaking

---

## Data Fetching

| Library | Version | Status | Note |
|---------|---------|--------|------|
| TanStack Query | 5.x | **Latest** | 5.90.x, React 19 support |

> **Note:**
> - TanStack Query v6 chỉ có cho Svelte, React vẫn dùng v5
> - Zero-config caching, background updates
> - Infinite loading APIs built-in

---

## HTTP Client

| Library | Version | Status | Note |
|---------|---------|--------|------|
| Axios | 1.x | Stable | HTTP client, interceptors |

**Alternative:** `ky` hoặc native `fetch` với TanStack Query

---

## Summary - Recommended Versions

```json
// package.json
{
  "dependencies": {
    "react": "^19.2.0",
    "react-dom": "^19.2.0",
    "@mantine/core": "^8.3.0",
    "@mantine/hooks": "^8.3.0",
    "@mantine/form": "^8.3.0",
    "@mantine/dropzone": "^8.3.0",
    "@mantine/notifications": "^8.3.0",
    "react-router": "^7.12.0",
    "@tanstack/react-query": "^5.90.0",
    "axios": "^1.7.0"
  },
  "devDependencies": {
    "vite": "^7.3.0",
    "typescript": "^5.7.0",
    "@types/react": "^19.0.0",
    "@types/react-dom": "^19.0.0",
    "postcss": "^8.0.0",
    "postcss-preset-mantine": "^1.17.0",
    "postcss-simple-vars": "^7.0.0"
  }
}
```

### Vite Config

```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  css: {
    postcss: {
      plugins: [
        require('postcss-preset-mantine'),
        require('postcss-simple-vars')({
          variables: {
            'mantine-breakpoint-xs': '36em',
            'mantine-breakpoint-sm': '48em',
            'mantine-breakpoint-md': '62em',
            'mantine-breakpoint-lg': '75em',
            'mantine-breakpoint-xl': '88em',
          },
        }),
      ],
    },
  },
})
```

---

## Folder Structure
Tự cập nhật

## Version Check Sources

- React: [react.dev/versions](https://react.dev/versions)
- Vite: [vite.dev/releases](https://vite.dev/releases)
- Mantine: [mantine.dev/changelog](https://mantine.dev/changelog/all-releases/)
- React Router: [reactrouter.com/changelog](https://reactrouter.com/changelog)
- TanStack Query: [GitHub Releases](https://github.com/TanStack/query/releases)
