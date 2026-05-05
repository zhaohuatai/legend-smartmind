const toasts = []
let idCounter = 0
let listeners = []

function notifyListeners() {
  listeners.forEach(fn => fn([...toasts]))
}

function showToast(title, message, type = 'info', duration = 3000) {
  const id = ++idCounter
  toasts.push({ id, title, message, type })
  notifyListeners()
  setTimeout(() => {
    const idx = toasts.findIndex(t => t.id === id)
    if (idx > -1) toasts.splice(idx, 1)
    notifyListeners()
  }, duration)
}

export function useToast() {
  const getToasts = () => [...toasts]
  const subscribe = (fn) => {
    listeners.push(fn)
    return () => {
      listeners = listeners.filter(l => l !== fn)
    }
  }
  return { getToasts, subscribe }
}

export function toast(title, message, duration) {
  showToast(title, message, 'info', duration)
}

export function toastSuccess(message, duration) {
  showToast('操作成功', message, 'success', duration)
}

export function toastError(message, duration) {
  showToast('操作失败', message, 'error', duration)
}

export function toastWarning(message, duration) {
  showToast('提示', message, 'warning', duration)
}
