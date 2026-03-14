import { Routes, Route, Navigate } from 'react-router-dom'
import MainLayout from './layouts/MainLayout'
import LoginPage from './pages/auth/LoginPage'
import HomePage from './pages/home/HomePage'
import SearchPage from './pages/search/SearchPage'
import MessagesPage from './pages/messages/MessagesPage'
import ProfilePage from './pages/profile/ProfilePage'
import { isAuthenticated } from './hooks/useAuth'

function GuestRoute({ children }: { children: React.ReactNode }) {
  if (isAuthenticated()) {
    return <Navigate to="/" replace />
  }
  return <>{children}</>
}

export default function App() {
  return (
    <Routes>
      <Route
        path="/login"
        element={
          <GuestRoute>
            <LoginPage />
          </GuestRoute>
        }
      />
      <Route element={<MainLayout />}>
        <Route path="/" element={<HomePage />} />
        <Route path="/search" element={<SearchPage />} />
        <Route path="/explore" element={<SearchPage />} />
        <Route path="/reels" element={<SearchPage />} />
        <Route path="/messages" element={<MessagesPage />} />
        <Route path="/notifications" element={<HomePage />} />
        <Route path="/create" element={<HomePage />} />
        <Route path="/profile" element={<ProfilePage />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
